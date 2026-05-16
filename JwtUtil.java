@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT令牌
     */
    public String generateToken(Long userId, String username, String role) {
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证并解析JWT令牌
     */
    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserId(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("userId").asLong();
        }
        return null;
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsername(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("username").asString();
        }
        return null;
    }

    /**
     * 从令牌中获取用户角色
     */
    public String getRole(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("role").asString();
        }
        return null;
    }

    /**
     * 检查令牌是否过期
     */
    public boolean isExpired(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getExpiresAt().before(new Date());
        }
        return true;
    }
}
