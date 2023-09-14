package app.statics.cargos;

import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

import static app.statics.Basics.ygd;

public interface Perms {
    Role cargoOp = ygd.getRoleById("1144701094337978480");
    Role tresEstrelas = ygd.getRoleById("1145504390975864932");
    Role duasEstrelas = ygd.getRoleById("1146466318317539338");

    static List<Role> getPermBan() {
        List<Role> permBanList = new ArrayList<>();
        permBanList.add(cargoOp);
        permBanList.add(tresEstrelas);
        // Adicione outros cargos à lista, se necessário
        return permBanList;
    }

    static List<Role> getPermCargo() {
        List<Role> permCargoList = new ArrayList<>();
        permCargoList.add(cargoOp);
        permCargoList.add(tresEstrelas);
        permCargoList.add(duasEstrelas);
        // Adicione outros cargos à lista, se necessário
        return permCargoList;
    }


    }
