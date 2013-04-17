package com.zeedoo.api.users.hmac;

import static com.google.common.base.Preconditions.checkNotNull;

import java.security.MessageDigest;

import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;

/**
 * <p>Value object to provide the following to {@link org.multibit.mbm.auth.hmac.HmacAuthenticator}:</p>
 * <ul>
 * <li>Storage of the necessary credentials for HMAC authentication</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class HmacServerCredentials {
  private final String apiKey;
  private final String digest;
  private final String canonicalRepresentation;
  private final String algorithm;

  /**
   * @param algorithm               The algorithm used for computing the digest (e.g. "HmacSHA1", "HmacSHA256", "HmacSHA512")
   * @param apiKey                  The API key used for looking up the shared secret key associated with the user
   * @param digest                  The digest of (contents + shared secret key)
   * @param canonicalRepresentation The canonical representation of the request that was signed
   * @param requiredAuthorities     The authorities required to authenticate (provided by the {@link org.multibit.mbm.auth.annotation.RestrictedTo} annotation)
   */
  public HmacServerCredentials(
    String algorithm,
    String apiKey,
    String digest,
    String canonicalRepresentation) {
    this.algorithm = checkNotNull(algorithm);
    this.apiKey = checkNotNull(apiKey);
    this.digest = checkNotNull(digest);
    this.canonicalRepresentation = checkNotNull(canonicalRepresentation);
  }

  /**
   * @return The name of the algorithm used to compute the digest
   */
  public String getAlgorithm() {
    return algorithm;
  }

  /**
   * @return The public API key (allows look up of shared secret key)
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * @return The digest of the request content and shared secret key
   */
  public String getDigest() {
    return digest;
  }

  /**
   * @return The canonical representation of the request that was signed
   */
  public String getCanonicalRepresentation() {
    return canonicalRepresentation;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final HmacServerCredentials that = (HmacServerCredentials) obj;

    final byte[] thisBytes = digest.getBytes(Charsets.UTF_8);
    final byte[] thatBytes = that.digest.getBytes(Charsets.UTF_8);
    return apiKey.equals(that.apiKey) && MessageDigest.isEqual(thisBytes, thatBytes);
  }

  @Override
  public int hashCode() {
    return (31 * apiKey.hashCode()) + digest.hashCode();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("apiKey", apiKey)
      .add("digest", digest)
      .add("contents", canonicalRepresentation)
      .toString();
  }

}
