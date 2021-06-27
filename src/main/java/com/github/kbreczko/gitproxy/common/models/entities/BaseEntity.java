package com.github.kbreczko.gitproxy.common.models.entities;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GITPROXY_SEQUENCE")
    @SequenceGenerator(name = "GITPROXY_SEQUENCE", sequenceName = "gitproxy_sequence", initialValue = 1, allocationSize = 30)
    private long id;
}
