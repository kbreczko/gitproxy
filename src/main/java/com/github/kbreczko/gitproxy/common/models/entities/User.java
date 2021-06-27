package com.github.kbreczko.gitproxy.common.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity {
    @Column(unique = true, nullable = false, length = 50, updatable = false)
    private String login;

    @Column(nullable = false)
    private long requestCount;
}
