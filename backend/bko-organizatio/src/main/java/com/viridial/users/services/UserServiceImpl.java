package com.viridial.users.services;

import com.viridial.users.entities.UserEntity;
import com.viridial.users.forms.UserForm;
import com.viridial.users.forms.UserSearchForm;
import com.viridial.users.mapper.UserMapper;
import com.viridial.users.repositories.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for UserEntity operations.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<UserForm> search(UserSearchForm search) {
        Pageable pageable = buildPageable(search);
        Page<UserEntity> page = userRepositoryCustom.search(search, pageable);
        return page.getContent().stream()
                .map(UserMapper::mapEntityToForm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int bulkUpdateActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return 0;
        List<UserEntity> entities = userRepositoryCustom.findAllById(ids);
        entities.forEach(entity -> entity.setActive(active));
        userRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional
    public int bulkDelete(List<Long> ids, String deletedBy) {
        if (ids == null || ids.isEmpty()) return 0;
        List<UserEntity> entities = userRepositoryCustom.findAllById(ids);
        if (deletedBy != null) {
            entities.forEach(entity -> entity.delete(deletedBy));
        } else {
            entities.forEach(UserEntity::delete);
        }
        userRepositoryCustom.saveAll(entities);
        return entities.size();
    }

    private Pageable buildPageable(UserSearchForm search) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        return PageRequest.of(page, size);
    }
}

