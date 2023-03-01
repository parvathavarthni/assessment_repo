package fun;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.pojodata;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class GameTests {

	public final static String Base_Url="http://localhost:8080/game?name=";
	@Test
	public void test_with_params()
	{
		String queryparams[]= {"Chess","Carom","Hockey"};
		for(String game:queryparams)
		{
			Response response= get(Base_Url+game);
			response.then()
			.log().all()
			.body("text",equalTo ("Playing " +game+ " is fun!"))
			.statusCode(200)
			.statusLine("HTTP/1.1 200 ")
			.contentType("application/json");
			pojodata dataclass= response.as(pojodata.class);  //JSON to POJO
	        assertNotNull(dataclass.getId());
	        assertTrue(dataclass.getId()>0);
	        assertTrue(dataclass.getText().contains(game));		
			
		}
	}
	@Test
		public void test_without_params()
		{
			String defaultgame="Sudoku";
				Response response= get(Base_Url);
				response.then()
				.log().all()
				.body("text",equalTo ("Playing "+defaultgame+" is fun!"))
				.statusCode(200)
				.statusLine("HTTP/1.1 200 ")
				.contentType("application/json");
				pojodata dataclass= response.as(pojodata.class); //JSON TO POJO
		        assertNotNull(dataclass.getId());
		        assertTrue(dataclass.getId()>0);
		        assertTrue(dataclass.getText().contains(defaultgame));		

	}
	
	@Test
	public void schema_check()
	{
		given().get(Base_Url).then().assertThat().body(JsonSchemaValidator.matchesJsonSchema
        (new File("src/test/java/fun/schema.json")));
	}	

}
