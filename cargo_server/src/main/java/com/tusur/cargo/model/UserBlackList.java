package com.tusur.cargo.model;

import java.time.Instant;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "blackList")
public class UserBlackList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long blackListId;

  private String message;

  private Date dateOfBlocking;

  private Date unlockDate;
}
