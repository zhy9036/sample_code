import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.FileInputStream;

/**
 * Runs queries against a back-end database
 */
public class Query {
	private String configFilename;
	private Properties configProps = new Properties();
	private int numLeft;
	private String jSQLDriver;
	private String jSQLUrl;
	private String cusUrl;
	private String jSQLUser;
	private String jSQLPassword;

	// DB Connection
	private Connection conn;
    private Connection customerConn;

	// Canned queries

	// LIKE does a case-insensitive match
	private static final String SEARCH_SQL_BEGIN =
		"SELECT * FROM movie WHERE name LIKE '%";
	private static final String SEARCH_SQL_END = 
		"%' ORDER BY id";
	private static final String STATUS_SQL = 
		"SELECT cid, status FROM Rental "
		+"where mid = ?";
	private static final String DIRECTOR_MID_SQL = "SELECT y.* "
					 + "FROM movie_directors x, directors y "
					 + "WHERE x.mid = ? and x.did = y.id";
	private static final String ACTOR_MID_SQL ="SELECT y.* "
					 + "FROM casts x, actor y "
					 + "WHERE x.mid = ? and x.pid = y.id";
	private PreparedStatement directorMidStatement;
 	private PreparedStatement statusMid;
 	private PreparedStatement actorMidStatement;
	/* uncomment, and edit, after your create your own customer database */
	
	private static final String CUSTOMER_LOGIN_SQL = 
		"SELECT * FROM customer WHERE login = ? and password = ?";
	private PreparedStatement customerLoginStatement;

	private static final String BEGIN_TRANSACTION_SQL = 
		"SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; BEGIN TRANSACTION;";
	private PreparedStatement beginTransactionStatement;

	private static final String COMMIT_SQL = "COMMIT TRANSACTION";
	private PreparedStatement commitTransactionStatement;

	private static final String ROLLBACK_SQL = "ROLLBACK TRANSACTION";
	private PreparedStatement rollbackTransactionStatement;
	
	

	public Query(String configFilename) {
		this.configFilename = configFilename;
	}

    /**********************************************************/
    /* Connection code to SQL Azure. Example code below will connect to the imdb database on Azure
       IMPORTANT NOTE:  You will need to create (and connect to) your new customer database before 
       uncommenting and running the query statements in this file .
     */

	public void openConnection() throws Exception {
		configProps.load(new FileInputStream(configFilename));

		jSQLDriver   = configProps.getProperty("videostore.jdbc_driver");
		jSQLUrl	   = configProps.getProperty("videostore.imdb_url");
		jSQLUser	   = configProps.getProperty("videostore.sqlazure_username");
		jSQLPassword = configProps.getProperty("videostore.sqlazure_password");

		cusUrl    = configProps.getProperty("videostore.customer_url");


		/* load jdbc drivers */
		Class.forName(jSQLDriver).newInstance();

		/* open connections to the imdb database */

		conn = DriverManager.getConnection(jSQLUrl, // database
						   jSQLUser, // user
						   jSQLPassword); // password
                
		conn.setAutoCommit(true); //by default automatically commit after each statement 

		/* You will also want to appropriately set the 
                   transaction's isolation level through:  */
		   conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		/* Also you will put code here to specify the connection to your
		   customer DB.  E.g.
        */
		   customerConn = DriverManager.getConnection(cusUrl, jSQLUser, jSQLPassword);
		   customerConn.setAutoCommit(true); //by default automatically commit after each statement
		   customerConn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 

	        
	}

	public void closeConnection() throws Exception {
		conn.close();
		customerConn.close();
	}

    /**********************************************************/
    /* prepare all the SQL statements in this method.
      "preparing" a statement is almost like compiling it.  Note
       that the parameters (with ?) are still not filled in */

	public void prepareStatements() throws Exception {

		directorMidStatement = conn.prepareStatement(DIRECTOR_MID_SQL);

		/* uncomment after you create your customers database */
	
		customerLoginStatement = customerConn.prepareStatement(CUSTOMER_LOGIN_SQL);
		beginTransactionStatement = customerConn.prepareStatement(BEGIN_TRANSACTION_SQL);
		commitTransactionStatement = customerConn.prepareStatement(COMMIT_SQL);
		rollbackTransactionStatement = customerConn.prepareStatement(ROLLBACK_SQL);
		

		/* add here more prepare statements for all the other queries you need */
		actorMidStatement = conn.prepareStatement(ACTOR_MID_SQL);
		statusMid = customerConn.prepareStatement(STATUS_SQL);
	}


    /**********************************************************/
    /* Suggested helper functions; you can complete these, or write your own
       (but remember to delete the ones you are not using!) */

	public int getRemainingRentals(int cid) throws Exception {

		String searchSql = "select pid from customer where id = " + cid;
		Statement searchStatement = customerConn.createStatement();
		ResultSet pdata = searchStatement.executeQuery(searchSql);
		pdata.next();

		String searchSql1 = "select maxnum from Plans where pid = " + pdata.getString(1);
		Statement searchStatement1 = customerConn.createStatement();
		ResultSet maxn = searchStatement1.executeQuery(searchSql1);
		maxn.next();
		int max = maxn.getInt(1);
		
		String searchSql2 = "select count(mid) as n from Rental where cid = " + cid + "and status = '1' ";
		Statement searchStatement2 = customerConn.createStatement();
		ResultSet cur = searchStatement2.executeQuery(searchSql2);
		cur.next();
		int curt = cur.getInt(1);
		cur.close();
		maxn.close();
		pdata.close();
		return max-curt;
	}
	public boolean availability(int mid) throws Exception {
		String searchSql = "select top 1 status from Rental where mid = " + mid+" order by time desc";
	    Statement searchStatement = customerConn.createStatement();
		ResultSet movie_set = searchStatement.executeQuery(searchSql);
		if(!movie_set.next()){
			//movie_set.next();
			movie_set.close();
			return true;
		}else{
			//movie_set.next();
			if(movie_set.getInt(1) == 1){
				movie_set.close();
				return false;
			}else{
				movie_set.close();
				return true;
			}
		}
	}

	public boolean isValidPlan(int planid) throws Exception{
		return true;
	}

	public boolean isValidMovie(int mid) throws Exception {
		/* is mid a valid movie ID?  You have to figure it out */
		String searchSql = "select * from movie where id = " + mid;
		Statement searchStatement = conn.createStatement();
		ResultSet pdata = searchStatement.executeQuery(searchSql);
		if(pdata.next()){
			return true;
		}else{
			return false;
		}
	}

    /**********************************************************/
    /* login transaction: invoked only once, when the app is started  */
	public int transaction_login(String name, String password) throws Exception {
		/* authenticates the user, and returns the user id, or -1 if authentication fails */

		/* Uncomment after you create your own customers database */
		
		int cid;

		customerLoginStatement.clearParameters();
		customerLoginStatement.setString(1,name);
		customerLoginStatement.setString(2,password);
		ResultSet cid_set = customerLoginStatement.executeQuery();
		if (cid_set.next()) cid = cid_set.getInt(1);
		else cid = -1;
		cid_set.close();
		return(cid);
		 
		//return (55);
	}

	public void transaction_printPersonalData(int cid) throws Exception {
		/* println the customer's personal data: name, and plan number */
		String searchSql = "select pid, fname, lname from customer where id = " + cid;
		Statement searchStatement = customerConn.createStatement();
		ResultSet pdata = searchStatement.executeQuery(searchSql);
		pdata.next();
		
		numLeft = getRemainingRentals(cid);
		System.out.println("Name: " + pdata.getString(3)
						+ " " + pdata.getString(2));
		System.out.println("plan id: " + pdata.getString(1));
		//numLeft = max-curt;
		System.out.println("Number of movies can be rent: " + numLeft);
		//maxn.close();
		pdata.close();
		//cur.close();
	}


    /**********************************************************/
    /* main functions in this project: */

	public void transaction_search(int cid, String movie_title)
			throws Exception {
		/* searches for movies with matching titles: SELECT * FROM movie WHERE name LIKE movie_title */
		/* prints the movies, directors, actors, and the availability status:
		   AVAILABLE, or UNAVAILABLE, or YOU CURRENTLY RENT IT */

		/* Interpolate the movie title into the SQL string */
		String searchSql = SEARCH_SQL_BEGIN + movie_title + SEARCH_SQL_END;
		
		Statement searchStatement = conn.createStatement();
		ResultSet movie_set = searchStatement.executeQuery(searchSql);
		while (movie_set.next()) {
			int mid = movie_set.getInt(1);
			System.out.println("ID: " + mid + " NAME: "
					+ movie_set.getString(2) + " YEAR: "
					+ movie_set.getString(3));
			/* do a dependent join with directors */
			directorMidStatement.clearParameters();
			directorMidStatement.setInt(1, mid);
			ResultSet director_set = directorMidStatement.executeQuery();
			while (director_set.next()) {
				System.out.println("\t\tDirector: " + director_set.getString(3)
						+ " " + director_set.getString(2));
			}
			director_set.close();

			actorMidStatement.clearParameters();
			actorMidStatement.setInt(1, mid);
			ResultSet actor_set = actorMidStatement.executeQuery();
			while (actor_set.next()) {
				System.out.println("\t\tActor: " + actor_set.getString(3)
						+ " " + actor_set.getString(2));
			}
			actor_set.close();
			//************************************************************

			
			statusMid.setInt(1, mid);
		
			ResultSet status = statusMid.executeQuery();
			boolean found = false;
			
			System.out.print("\t\tStatus: ");
			while(status.next()&& !found){
				int thiscid = status.getInt(1);
				String st = status.getString(2);
				if(thiscid == cid){
					if(st.equals("1")){
						System.out.println("YOU HAVE IT");
						found = true;
					}
				}else{
					if(st.equals("1")){
						System.out.println("UNAVAILABLE");
						found = true;
					}
				}
			}				
			if(!found){
				System.out.println("AVAILABLE");
		
			}
			
			/* now you need to retrieve the actors, in the same manner */
			/* then you have to find the status: of "AVAILABLE" "YOU HAVE IT", "UNAVAILABLE" */
		}
		movie_set.close();
		System.out.println();
	}

	public void transaction_choosePlan(int cid, int pid) throws Exception {
	    /* updates the customer's plan to pid: UPDATE customer SET plid = pid */
	    /* remember to enforce consistency ! */
	    System.out.println();
	    if(pid>8 || pid<0){
	    	System.out.println("Please choose a vaild plan number");
	    }else{
	    	//beginTransactionStatement = customerConn.prepareStatement(BEGIN_TRANSACTION_SQL);
	    	beginTransaction();
	    	String searchSql = "UPDATE customer SET pid = "+ pid + "where id = "+cid;
			PreparedStatement searchStatement = customerConn.prepareStatement(searchSql);
			searchStatement.executeUpdate();
	    	int remins = getRemainingRentals(cid);
	    	if(remins<0){
	    		rollbackTransaction();
	    		System.out.println("Fail");
	    	}else{
	    		commitTransaction();
	    		System.out.println("Changed successfully");
	    	}
	    }
	}

	public void transaction_listPlans() throws Exception {
	    /* println all available plans: SELECT * FROM plan */
	 	String searchSql = "select * from Plans order by pid asc";
		Statement searchStatement = customerConn.createStatement();
		ResultSet cur = searchStatement.executeQuery(searchSql);
		while(cur.next()){
			System.out.println("plan name: "+cur.getString(2));
			System.out.println("\tid: " + cur.getString(1));			
			System.out.println("\tmax: "+cur.getString(3));
			System.out.println("\tmonthly fee: "+cur.getString(4));
			System.out.println();
		}
		cur.close();

	}

	public void transaction_rent(int cid, int mid) throws Exception {
	    /* rent the movie mid to the customer cid */
	    /* remember to enforce consistency ! */				
	    System.out.println();
	    
	    if(!isValidMovie(mid)){
			System.out.println("NO SUCH MOVIE");
	    	return;
	    }
		
	    if(numLeft == 0){
	    	System.out.println("You can not RENT anymore.");
	    	return;
	    }
	    beginTransaction();
	    String searchSql = "select top 1 status, cid from Rental where mid = " + mid+" order by time desc";
	    Statement searchStatement = customerConn.createStatement();
		ResultSet movie_set = searchStatement.executeQuery(searchSql);

		String rentNew = "insert into Rental values("+cid+","+mid+ ","+1+",GETDATE())";
		PreparedStatement rentStatement1 = customerConn.prepareStatement(rentNew);

		boolean flag = availability(mid);
		if(!movie_set.next()){
			rentStatement1.executeUpdate();
			if(getRemainingRentals(cid)<0){
				rollbackTransaction();
				System.out.println("Fail");
			}else{
				commitTransaction();
				System.out.println("Congrates! You rented it successfully.");
			}
		}else{
			//movie_set.next();	
			if(movie_set.getInt(2) == cid){
				if(movie_set.getInt(1) == 1){
					System.out.println("You Have Already Rented It!");
					rollbackTransaction();
				}else{
					// update -> rent
					rentStatement1.executeUpdate();
					if(getRemainingRentals(cid)<0 || !flag){
						rollbackTransaction();
						System.out.println("Fail");
					}else{
						commitTransaction();
						System.out.println("Congrates! You rented it successfully. Thank you!");
					}
				}
			}else{
				if(movie_set.getInt(1) == 1){
					rollbackTransaction();
					System.out.println("UNAVAILABLE, CAN NOT BE RENTED AT THIS TIME");
				}else{
					// update -> rent
					rentStatement1.executeUpdate();
					if(getRemainingRentals(cid)<0 || !flag){
						rollbackTransaction();
						System.out.println("Fail");
					}else{
						commitTransaction();
						System.out.println("Congrates! You rented it successfully.");
					}
				}
			}
		}
		movie_set.close();

	}

	public void transaction_return(int cid, int mid) throws Exception {
	    /* return the movie mid by the customer cid */
	    System.out.println();
	    
	    //transaction mangement
	    beginTransaction();

	    String searchSql = "select top 1 status from Rental where cid = " + cid + " and mid = " + mid+" order by time desc";
	    Statement searchStatement = customerConn.createStatement();
		ResultSet movie_set = searchStatement.executeQuery(searchSql);

		String returned = "UPDATE Rental SET status = 0 where cid = "+cid+" and mid =" + mid;
		PreparedStatement rentStatement = customerConn.prepareStatement(returned);

		if(!movie_set.next()){
			System.out.println("You have never rented this movie");
			movie_set.close();
			rollbackTransaction();
		}else{
			if(movie_set.getInt(1) == 1){
				rentStatement.executeUpdate();
				movie_set.close();
				System.out.println("Thank you! Retruned successfully!");
				commitTransaction();
			}else{
				System.out.println("You have already returned this movie");
				movie_set.close();
				rollbackTransaction();
			}
		}
	}

	public void transaction_fastSearch(int cid, String movie_title)
			throws Exception {
		/* like transaction_search, but uses joins instead of dependent joins
		   Needs to run three SQL queries: (a) movies, (b) movies join directors, (c) movies join actors
		   Answers are sorted by mid.
		   Then merge-joins the three answer sets */
		String searchSql = SEARCH_SQL_BEGIN + movie_title + SEARCH_SQL_END;
		String searchSqlthis = SEARCH_SQL_BEGIN + movie_title + "%' ";
		String searchSql2 = "select x.*, z.id from " 
		         +"MOVIE_DIRECTORS y join directors x on x.id = y.did "
				+"right join ("+ searchSqlthis + ") as z on z.id = y.mid order by z.id asc";
		
		String searchSql3 = "select x.*, z.id from casts y join actor x on x.id = y.pid "
							+"right join ("+searchSqlthis+") as z on z.id = y.mid order by z.id asc";
		
		PreparedStatement searchStatement = conn.prepareStatement(searchSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		PreparedStatement searchStatement1 = conn.prepareStatement(searchSql2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		PreparedStatement searchStatement2 = conn.prepareStatement(searchSql3,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet movie_set = searchStatement.executeQuery();
		ResultSet director_set = searchStatement1.executeQuery();
		ResultSet actor_set = searchStatement2.executeQuery();
		while (movie_set.next()) {
			int mid = movie_set.getInt(1);
			System.out.println("ID: " + mid + " NAME: "
					+ movie_set.getString(2) + " YEAR: "
					+ movie_set.getString(3));
			/* do a dependent join with directors */
			while (director_set.next()&&mid == director_set.getInt(4)){
					System.out.println("\t\tDirector: " + director_set.getString(3)
							+ " " + director_set.getString(2));
			}		
			director_set.previous();
			while (actor_set.next()&&mid == actor_set.getInt(5)){
				 
				System.out.println("\t\tActor: " + actor_set.getString(3)
						+ " " + actor_set.getString(2));
			}
			actor_set.previous();
		}
		director_set.close();
		actor_set.close();
		movie_set.close();
		System.out.println();
	}


    /* Uncomment helpers below once you've got beginTransactionStatement,
       commitTransactionStatement, and rollbackTransactionStatement setup from
       prepareStatements():
    */
    public void beginTransaction() throws Exception {
	    customerConn.setAutoCommit(false);
	    beginTransactionStatement.executeUpdate();	
    }

    public void commitTransaction() throws Exception {
	    commitTransactionStatement.executeUpdate();	
	    customerConn.setAutoCommit(true);
	}
    public void rollbackTransaction() throws Exception {
	    rollbackTransactionStatement.executeUpdate();
	    customerConn.setAutoCommit(true);
	} 
    

}
