package ru.poly.studentstestingsystem.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.poly.studentstestingsystem.dto.GroupDto;
import ru.poly.studentstestingsystem.service.GroupService;

@RestController
@RequestMapping(path = "api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getStudents() {
        return groupService.getGroups();
    }

    @GetMapping(path = "{id}")
    public GroupDto getStudentById(@PathVariable("id") long id) {
        return groupService.getGroupById(id);
    }
}
