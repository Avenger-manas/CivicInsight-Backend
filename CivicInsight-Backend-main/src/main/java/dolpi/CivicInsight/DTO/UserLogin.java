package dolpi.CivicInsight.DTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
