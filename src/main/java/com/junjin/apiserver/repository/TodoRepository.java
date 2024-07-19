package com.junjin.apiserver.repository;

import com.junjin.apiserver.domain.Todo;
import com.junjin.apiserver.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {

}
