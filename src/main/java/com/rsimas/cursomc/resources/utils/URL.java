package com.rsimas.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	//Retira os espaços em branco de uma string;
	public static String decodeParam(String s) {
		
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	//Transforma String em Inteiros
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i = 0 ; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		
		return list;
		
		//OU
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.ToList());
	}
}
