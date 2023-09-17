package app.statics.cargos;

import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

import static app.statics.Basics.ygd;

public interface Hierarquia {

    Role denier = ygd.getRoleById("1145374321062191177");
    Role refuter = ygd.getRoleById("1145909465640603708");
    Role nonbeliever = ygd.getRoleById("1145909656691150848");
    Role rejector = ygd.getRoleById("1145847042426536048");
    Role disavower = ygd.getRoleById("1145909405498478794");
    Role dissenter = ygd.getRoleById("1145909563867017246");
    Role contrarian = ygd.getRoleById("1145909732431888534");
    Role discordant = ygd.getRoleById("1145909820768145429");
    Role negator = ygd.getRoleById("1145909911780327484");
    Role disprover = ygd.getRoleById("1145910006827454515");

    static List<Role> getHierarquia() {
        List<Role> hierarquia = new ArrayList<>();

        hierarquia.add(denier);
        hierarquia.add(refuter);
        hierarquia.add(nonbeliever);
        hierarquia.add(rejector);
        hierarquia.add(disavower);
        hierarquia.add(dissenter);
        hierarquia.add(contrarian);
        hierarquia.add(discordant);
        hierarquia.add(negator);
        hierarquia.add(disprover);

        return hierarquia;
    }



}
