package ru.poly.studentstestingsystem.service;

import ru.poly.studentstestingsystem.dto.GroupDto;

import java.util.List;

public interface GroupService {
    List<GroupDto> getGroups();

    GroupDto getGroupById(long id);
}
