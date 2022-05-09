package ru.poly.studentstestingsystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.poly.studentstestingsystem.dto.GroupDto;
import ru.poly.studentstestingsystem.exception.StudentNotFoundException;
import ru.poly.studentstestingsystem.mapper.GroupMapper;
import ru.poly.studentstestingsystem.repository.GroupRepository;
import ru.poly.studentstestingsystem.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    private static final String GROUP_NOT_FOUND_MESSAGE = "Группа с id %s не найдена!";

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public List<GroupDto> getGroups() {
        return groupRepository.findAll().stream()
                .map(groupMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto getGroupById(long id) {
        return groupMapper.map(groupRepository.findById(id).orElseThrow(() ->
                new StudentNotFoundException(String.format(GROUP_NOT_FOUND_MESSAGE, id))));
    }
}
