package org.example.controllers;

import jakarta.validation.Valid;
import org.example.config.MySpringMVCDispatcherServletInitializer;
import org.example.dao.PersonDAO;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) throws SQLException {
        model.addAttribute("people",personDAO.index());

        return "people/index";
    }

    @GetMapping("/{id}") // туда можно поместить любое число и это число поместиться в аргументы метода
    public String show(@PathVariable(value = "id") int id, Model model) throws SQLException { // PathVariable примет этот id
        // Получим одного человека из дао и отдадим на представление
        model.addAttribute("person", personDAO.show(id));
        System.out.println(personDAO.show(id));
        return "people/show";

    }

    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }
    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) throws SQLException {    // bindingResult должен идти следующим аргументом после @Valid
        if (bindingResult.hasErrors()){
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) throws SQLException {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PostMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) throws SQLException {
        if (bindingResult.hasErrors()){
            return "people/edit";
        }
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) throws SQLException {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
