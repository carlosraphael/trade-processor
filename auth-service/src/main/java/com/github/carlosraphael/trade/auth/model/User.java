package com.github.carlosraphael.trade.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.github.carlosraphael.trade.model.Model;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User extends Model {

	private static final long serialVersionUID = -6389537374689291664L;

	@NotNull @Email 
	@Column(unique = true)
	private String email;
	@NotNull @Size(min = 6)
	private String password;
}
