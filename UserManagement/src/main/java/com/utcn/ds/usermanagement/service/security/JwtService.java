package com.utcn.ds.usermanagement.service.security;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    /**
     * Extracts the username from a given token.
     *
     * @param token the token from which to extract the username
     * @return the username extracted from the token
     */
    String extractUsername(String token);

    /**
     * Extracts a specific claim from a given token using a ClaimsResolver function.
     *
     * @param token          the token from which to extract the claim
     * @param claimsResolver the ClaimsResolver function to use for extracting the claim
     * @param <T>            the type of the claim to extract
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates a token for the given user details.
     *
     * @param userDetails the user details to generate a token for
     * @return the generated token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a token with specified claims for the given user details.
     *
     * @param extractClaims the claims to include in the token
     * @param userDetails   the user details to generate a token for
     * @return the generated token
     */
    String generateToken(Map<String, Object> extractClaims, UserDetails userDetails);

    /**
     * Checks whether a given token is valid for the given user details.
     *
     * @param token       the token to validate
     * @param userDetails the user details to validate the token against
     * @return true if the token is valid for the given user details, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
