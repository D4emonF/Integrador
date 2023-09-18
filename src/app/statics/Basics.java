package app.statics;


import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.List;

import static app.App.jda;


public interface Basics {
    String token = "MTE0NDcwMjAyMzk4MDMwMjQxNw.GpyLct.Dst1Q5XQCBzbaRfkJwLnrJ1I_DQypzAlNdFPsc";
    String prefixo = "lc!";
    List<String> prefixos = Arrays.asList("m!", "+", "s!");
    Guild ygd = jda.getGuildById("1144682628532949154");
    Guild rascunhos = jda.getGuildById("1096975449709629530");

}
