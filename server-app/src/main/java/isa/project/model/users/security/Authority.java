package isa.project.model.users.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Authority")
public class Authority implements GrantedAuthority {
	private static final long serialVersionUID = 659222305783118054L;
	
	@Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;   
    
    public Authority(String name) {
    	this.name = name;
	}

	@Override
	public String getAuthority() {
		return name;
	}
}
