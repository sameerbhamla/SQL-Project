import java.util.Scanner;
import java.sql.*;

public class SQLProject {
	
	//Global User Account: Username Sameer Password cracras77
	//LocalAccount User: LocalUser password: test12
	
    private static final String host="100.2.94.121";//"100.2.94.121";
    private static final String DB_URL = "jdbc:mysql://"+host+":3306/331table";
    private static final String USER = "Sameer";//"Sameer";
    private static final String PASS = "cracras77";//"cracras77";
    private static final String[] unsafeKeywords = {
            "DROP", "DELETE", "UPDATE", "ALTER", "TRUNCATE", // DDL statements
            "INSERT", "MERGE", "CALL", // DML statements
            "GRANT", "REVOKE" // DCL statements

    };

    private static Connection conn=null;

    public static void main(String[] args) {


        connectDatabase();
        createTablesIfNeeded();



        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n\nPlease select an option: ");
            System.out.println("1. Specify a problem");
            System.out.println("2. Contribute SQL");
            System.out.println("3. Display a list of runnable queries");
            System.out.println("4. Delete Problem");
            System.out.println("5. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // Specify problem
                    specifyProblem(scanner);
                    break;
                case "2":
                    // Contribute SQL
                    contributeSql(scanner);
                    break;
                case "3":
                    // Display list of runnable queries
                    displayRunnableQueries(scanner);
                    break;
                case "4":
                    //delete
                    deleteProblem(scanner);

                    break;

                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }

        scanner.close();
    }

    private static void specifyProblem(Scanner scanner) {
        System.out.println("\nPlease enter the description of your problem:");
        String problemDescription = scanner.nextLine();

        try {
            String sql = "INSERT INTO problems (description) VALUES (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, problemDescription);
            preparedStatement.executeUpdate();
            System.out.println("\nProblem specified and stored in database: " + problemDescription);
        } catch (SQLException e) {
            System.out.println("\nError occurred while storing problem in database:"+e);
            e.printStackTrace();
        }
    }


    private static void contributeSql(Scanner scanner) {
        try {
            String sql = "SELECT id, description FROM problems WHERE sql_query IS NULL";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n\n \t Problems List");

            while(rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("description"));
            }

            System.out.println("\n\nPlease enter the number of the problem you want to contribute SQL for:");
            int problemNumber = Integer.parseInt(scanner.nextLine());
            String sqlQuery="";

            do{
                System.out.println("\nPlease enter your SQL query:");
                sqlQuery = scanner.nextLine();

                if(isUnsafeInput(sqlQuery))
                    System.out.println("Unsafe SQL statement detected. Please enter a safe query.");


            }while(isUnsafeInput(sqlQuery));
            //check for unsafe sql and show error) while isunsafe



            sql = "UPDATE problems SET sql_query = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, sqlQuery);
            preparedStatement.setInt(2, problemNumber);
            preparedStatement.executeUpdate();

            System.out.println("\nSQL contributed for problem " + problemNumber + ": " + sqlQuery);
        } catch (SQLException e) {
            System.out.println("\nError occurred while contributing SQL");
            e.printStackTrace();
        }
    }


    public static boolean isUnsafeInput(String userInput) {
        for (String keyword : unsafeKeywords) {
            if (userInput.toUpperCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private static void displayRunnableQueries(Scanner scanner) {
        try {
            // Fetch and display list of problem specifications with corresponding SQLs
            String sql = "SELECT id, description FROM problems WHERE sql_query IS NOT NULL";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n\n \t Problems List");

            while(rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("description"));
            }

            // Get user input on which problem to run the query for
            System.out.println("\n\nPlease enter the number of the problem you want to run the query for:");
            int problemNumber = Integer.parseInt(scanner.nextLine());

            // Fetch the SQL query associated with the selected problem
            sql = "SELECT sql_query FROM problems WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, problemNumber);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String sqlQuery = rs.getString("sql_query");

                // Get user input on the parameter values for the SQL query
                System.out.println("\nPlease enter the parameter values for the SQL query (comma-separated):");
                String parameterValues = scanner.nextLine();

                if(!parameterValues.contains(","))
                    parameterValues+=",";

                // Split the parameters by comma and trim any whitespace
                String[] parameters = parameterValues.split(",");
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = parameters[i].trim();
                }

                // Prepare the SQL statement with the provided parameters
                preparedStatement = conn.prepareStatement(sqlQuery);
                for (int i = 0; i < parameters.length; i++) {
                    preparedStatement.setString(i + 1, parameters[i]);
                }

                // Execute the SQL query and display the results
                rs = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println("");
                }
            } else {
                System.out.println("\n\nNo SQL query found for the selected problem");
            }
        } catch (SQLException e) {
            System.out.println("\n\nError occurred while displaying runnable queries");
            e.printStackTrace();
        }
    }



    public static void connectDatabase()
    {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void createTablesIfNeeded() {
        try {
            Statement stmt = conn.createStatement();

            // Table for problems
            String sqlProblems = "CREATE TABLE IF NOT EXISTS problems (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "description TEXT NOT NULL,sql_query TEXT)";
            stmt.execute(sqlProblems);


        } catch (SQLException e) {
            System.out.println("Unable to create tables");
            e.printStackTrace();
        }
    }

    private static void deleteProblem(Scanner scanner) {
        try {
            String sql = "SELECT id, description FROM problems";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n\n \t Problems List");

            while(rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("description"));
            }

            System.out.println("\n\nPlease enter the number of the problem you want to delete");
            int problemNumber = Integer.parseInt(scanner.nextLine());



            sql = "delete from problems WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, problemNumber);
            preparedStatement.executeUpdate();

            System.out.println("\nProblem deleted Sucessfully . id : " + problemNumber );
        } catch (SQLException e) {
            System.out.println("\nError occurred while deleting Problem");
            e.printStackTrace();
        }


    }
}