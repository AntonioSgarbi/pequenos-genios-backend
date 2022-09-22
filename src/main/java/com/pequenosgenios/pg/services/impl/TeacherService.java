package com.pequenosgenios.pg.services.impl;

import com.pequenosgenios.pg.domain.Teacher;
import com.pequenosgenios.pg.dto.AddressDTO;
import com.pequenosgenios.pg.dto.NewTeacherDTO;
import com.pequenosgenios.pg.dto.TeacherDTO;
import com.pequenosgenios.pg.repositories.TeacherRepository;
import com.pequenosgenios.pg.services.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final AddressService addressService;

    public TeacherService(TeacherRepository teacherRepository, AddressService addressService) {
        this.teacherRepository = teacherRepository;
        this.addressService = addressService;
    }

    @Transactional(rollbackFor = Exception.class)
    public NewTeacherDTO insert(NewTeacherDTO newTeacherDTO) {
        Teacher model = new Teacher(newTeacherDTO);
        model = this.teacherRepository.save(model);
        newTeacherDTO.setId(model.getId());
        return newTeacherDTO;
    }

    @Transactional(readOnly = true)
    public Page<NewTeacherDTO> findAll(Pageable pageable) {
        return this.teacherRepository.findAll(pageable).map(NewTeacherDTO::new);
    }

    @Transactional(readOnly = true)
    public TeacherDTO findById(Long id) {
        return new TeacherDTO(this.findModel(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public TeacherDTO update(Long id, TeacherDTO teacherDTO) {
        TeacherDTO fromDatabase = this.findById(id);
        Util.myCopyProperties(teacherDTO, fromDatabase);
        this.teacherRepository.save(new Teacher(fromDatabase));
        return teacherDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.teacherRepository.delete(this.findModel(id));
    }

    protected Teacher findModel(Long id) {
        return this.teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

}
