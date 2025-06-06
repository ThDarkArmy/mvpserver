package tda.darkarmy.mvpserver.config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tda.darkarmy.mvpserver.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class CustomUser implements UserDetails, Serializable {

	private User user;

    public CustomUser(User user) {
        this.user = user;
    }

	@Override public Collection<? extends GrantedAuthority> getAuthorities()
	{
		List<GrantedAuthority> authorities = new ArrayList<>();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole());
		authorities.add(authority);
		return authorities;
	}

	@Override public String getPassword()
	{
		return user.getPassword();
	}

	@Override public String getUsername()
	{
		return user.getEmail();
	}

	@Override public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override public boolean isEnabled()
	{
		return true;
	}
}
