package br.com.zenontech.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.zenontech.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var servletPath = request.getServletPath();

    if (servletPath == "/tasks/") {
      var authorization = request.getHeader("Authorization");
      var auth_encoded = authorization.substring("Basic".length()).trim();

      byte[] auth_decoded = Base64.getDecoder().decode(auth_encoded);

      var auth_string = new String(auth_decoded);

      String[] credentials = auth_string.split(":");
      String username = credentials[0];
      String password = credentials[1];

      var user = this.userRepository.findByUsername(username);

      if (user == null) {
        response.sendError(401, "User does not exist");
      } else {
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (passwordVerify.verified) {
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401, "Unathourized password");
        }

      }

    }

    else {
      filterChain.doFilter(request, response);
    }

  }

}
