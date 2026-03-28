package com.dojo.customers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="customers")
@Data
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(name="last_name")
	private String lastName;
	private String username;
	@Column(name="cellphone")
	private String cellPhone;
//	@Lob
//	@JsonIgnore
//	private byte[] photo;
//
//	public Integer getPhotoHashCode(){
//		return (this.photo!=null) ? this.photo.hashCode():null;
//	}

	@Override
	public String toString() {
		return "Customer{" +
				"name='" + name + '\'' +
				", lastName='" + lastName + '\'' +
				", username='" + username + '\'' +
				", cellPhone='" + cellPhone + '\'' +
				'}';
	}
}
