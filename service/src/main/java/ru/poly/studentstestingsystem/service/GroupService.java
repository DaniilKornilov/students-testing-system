package ru.poly.studentstestingsystem.service;

import java.util.List;
import ru.poly.studentstestingsystem.dto.GroupDto;

public interface GroupService {

    List<GroupDto> getGroups();

    GroupDto getGroupById(long id);
}
