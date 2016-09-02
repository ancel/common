package com.work.common.demo;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.work.common.utils.db.AbstractDAO;

public class DaoDemo {
	public static void main(String[] args) {
		
	}
	
	class PersonDao extends AbstractDAO{

		@Override
		public String getConfigName() {
			// TODO Auto-generated method stub
			return "dataSourceName";
		}
		
		public List<Person> queryList() throws SQLException{
			String sql = "select person_id As personId from person";
			return runner.query(sql,  new BeanListHandler<Person>(Person.class));
		}
		
		public Person queryById(int personId) throws SQLException{
			String sql = "select person_id As personId from person where person_id="+personId;
			return runner.query(sql, new BeanHandler<Person>(Person.class));
		}
		
		public int updatePerson(Person person) throws SQLException{
			String sql = "update person set name=? where person_id=?";
			return runner.update(sql, new Object[] { person.getName(), person.getPersonId() });
		}
		
		public int insertPerson(Person person) throws SQLException{
			String sql = "insert into person (name) values (?)";
			return runner.update(sql, new Object[] { person.getName()});
		}
		
		public String getName(int personId) throws SQLException {
			String sql = "select name from person where person_id="+personId+"";
			return (String) runner.query(sql, new ScalarHandler<String>(1));
		}
		
		public List<String> getNames(String name) throws SQLException {
			String sql = "select name from person where name=?";
			return runner.query(sql, new ColumnListHandler<String>(1),name);
		}
		
		
	}
	
	class Person{
		private int personId;
		private String name;
		public int getPersonId() {
			return personId;
		}
		public void setPersonId(int personId) {
			this.personId = personId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
