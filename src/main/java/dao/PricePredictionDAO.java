package dao;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PricePredictionDAO {
    public double predictPrice(String cropName, String region) {
        String sql = "SELECT price FROM historical_prices WHERE crop_name = ? AND region = ?";
        double total = 0;
        double weight = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cropName);
            ps.setString(2, region);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double price = rs.getDouble("price");
                weight += 1;
                total += price * weight;
            }

            return total / weight; // Weighted average
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
