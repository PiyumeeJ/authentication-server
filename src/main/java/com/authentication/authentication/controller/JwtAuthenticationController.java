package com.authentication.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.authentication.config.JwtTokenUtil;
import com.authentication.authentication.model.CustomUserDetails;
import com.authentication.authentication.model.JwtRequest;
import com.authentication.authentication.model.JwtResponse;
import com.authentication.authentication.model.Token;
import com.authentication.authentication.model.ValidatedToken;
import com.authentication.authentication.repository.UserRepository;
import com.authentication.authentication.service.AuthenticateService;
import com.authentication.authentication.service.JwtAdminUserDetailsService;
import com.authentication.authentication.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtAdminUserDetailsService jwtAdminUserDetailsService;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private UserRepository userRepository;



    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        String userName = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        String role = authenticationRequest.getRole();

        CustomUserDetails userDetails = authenticateService.loadUser(userName, password, role);
        final String token = jwtTokenUtil.generateToken(userDetails, role);
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUserId()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/validate")
    private ValidatedToken validateToken(@RequestBody Token authenticationRequest) {
        String token = authenticationRequest.getToken();

        if(!jwtTokenUtil.isTokenExpired(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String role = jwtTokenUtil.getRoleFromToken(token);
            if (username != null) {
                CustomUserDetails userDetails = null;
                if (role.equalsIgnoreCase("user")) {
                    userDetails = jwtUserDetailsService.loadUserByUsername(username);
                } else {
                    userDetails = jwtAdminUserDetailsService.loadUserByUsername(username);
                }
//                String role = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).findFirst().get();
                ValidatedToken validatedToken = new ValidatedToken();
                validatedToken.setRole(role.toLowerCase());
                validatedToken.setValidationStatus("passed");
                validatedToken.setUserId(userDetails.getUserId());
                return validatedToken;
            } else {
                ValidatedToken validatedToken = new ValidatedToken();
                validatedToken.setValidationStatus("failed");
                return validatedToken;
            }
        } else {
            ValidatedToken validatedToken = new ValidatedToken();
            validatedToken.setValidationStatus("expired");
            return validatedToken;
        }

    }
}
