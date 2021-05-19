/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 22:45
 */
package fr.debris.palatest.server;

import com.google.gson.Gson;
import fr.debris.palatest.common.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Mysql {

    public void recreateDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("create table if not exists waterGrinder(id int auto_increment primary key,player_name varchar(255) null,craft_item varchar(255) null,date timestamp default current_timestamp() null,world_name varchar(255) null,player_position text null);");
        statement.execute();

        connection.close();
    }

    public void waterGrinderDone(EntityPlayer player, String item, World world) throws SQLException, ClassNotFoundException {
        String sql = "INSERT into waterGrinder(player_name, craft_item, world_name, player_position) VALUE (?, ?, ?, ?);";

        Connection connection = getConnection();

        Gson gson = new Gson();
        ArrayList<Double> arrayList = new ArrayList<Double>();
        arrayList.add(player.posX);
        arrayList.add(player.posY);
        arrayList.add(player.posZ);
        String position = gson.toJson(arrayList);

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, player.getDisplayName());
        statement.setString(2, item);
        statement.setString(3, world.getWorldInfo().getWorldName());
        statement.setString(4, position);
        statement.execute();

        connection.close();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String username = Reference.MYSQL.USERNAME;
        String password = Reference.MYSQL.PASSWORD;
        String host = Reference.MYSQL.HOST;
        String port = Reference.MYSQL.PORT;
        String database = Reference.MYSQL.DATABASE;

        return DriverManager.getConnection(String.format("jdbc:mysql://%s:%s@%s:%s/%s", username, password, host, port, database));
    }
}
