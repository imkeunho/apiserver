package com.junjin.apiserver.service;

import com.junjin.apiserver.dto.PageRequestDTO;
import com.junjin.apiserver.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet() {

        Long tno = 50L;

        log.info(todoService.get(tno));
    }

//    @Test
    public void todoRegister() {

        TodoDTO todoDTO = TodoDTO.builder()
                .title("title.....")
                .content("Content.....")
                .dueDate(LocalDate.of(2024, 12, 31))
                .build();

        log.info(todoService.register(todoDTO));
    }

    @Test
    public void testGetList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(21).build();

        log.info(todoService.getList(pageRequestDTO));
    }
}
