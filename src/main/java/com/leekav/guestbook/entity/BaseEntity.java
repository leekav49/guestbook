package com.leekav.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//Entity로 사용할 수는 있지만 테이블을 생성하지는 않음
@MappedSuperclass
//Jpa를 감시하고 있다가 데이터를 수정
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {
    //데이터 생성 날짜를 설정
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;

    //데이터의 마지막 수정 날짜를 설정
    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}
