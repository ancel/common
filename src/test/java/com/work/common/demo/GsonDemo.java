package com.work.common.demo;

import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonDemo {
	
	public static void main(String[] args) {
		People p = new People();
        //Gson gson = new Gson();
        //If you wanna allow null object
        Gson gson = new GsonBuilder().serializeNulls().create();
        String str = gson.toJson(p);
        System.out.println(str);

        People p2 = gson.fromJson(str, People.class);
        System.out.println(p2.toString());
        
        JsonElement jelem = gson.fromJson(str, JsonElement.class);  
        JsonObject jsonOBject = jelem.getAsJsonObject();
        Set<Entry<String, JsonElement>> set = jsonOBject.entrySet();
        for (Entry<String, JsonElement> entry : set) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
		
	}
	static class People{
		private String name;
		private String[] friends;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String[] getFriends() {
			return friends;
		}
		public void setFriends(String[] friends) {
			this.friends = friends;
		}
		
	}
}
