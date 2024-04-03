package com.cgmuniz.world;

public class Camera {
	
	public static int x;
	public static int y;
	
	public static int clamp(int Atual, int Min, int Max) {
		if(Atual < Min) {
			Atual = Min;
		}
		else if(Atual > Max) {
			Atual = Max;
		}
		
		return Atual;
	}
}
