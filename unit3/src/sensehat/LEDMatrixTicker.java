package sensehat;

import runtime.Scheduler;

public class LEDMatrixTicker {
	
	public java.lang.String pist;
	public int startno;
	public int red = 255;
	public int green = 255;
	public int blue = 255;
	public LEDPixelSet[] ps;
	public LEDMatrix matrix;
	public Scheduler scheduler;
	
	public LEDMatrixTicker(Scheduler s) {
		scheduler = s;
		matrix = new LEDMatrix(s);
	}
	
	public LEDMatrixTicker(Scheduler s, int r, int g, int b) {
		scheduler = s;
		matrix = new LEDMatrix(s);
		red = r;
		green = g;
		blue = b;
	}
	
	String getPixelSet(char c) {
		String s = "";
		switch (c) {
		case ' ':
			s = "01234567012345670123456701234567";
			break;
		case 'A':
			s = "01*****70*23*567*123*5670*23*56701*****701234567";
			break;
		case 'a':
			s = "01*34*6701*3*5*701*3*5*701*34*67012****701234567";
			break;
		case 'B':
			s = "*******7*12*45*7*12*45*7*12*45*70**3**6701234567";
			break;
		case 'b':
			s = "*******701*34*6701*345*701*345*7012***6701234567";
			break;
		case 'C':
			s = "0*****67*12345*7*12345*7*12345*70*234*6701234567";
			break;
		case 'c':
			s = "012***6701*345*701*345*701*345*701*345*701234567";
			break;
		case 'D':
			s = "*******7*12345*7*12345*7*12345*70*****6701234567";
			break;
		case 'd':
			s = "012***6701*345*701*345*701*34*67*******701234567";
			break;
		case 'E':
			s = "*******7*12*45*7*12*45*7*12*45*7*12345*701234567";
			break;
		case 'e':
			s = "012***6701*3*5*701*3*5*701*3*5*7012**56701234567";
			break;
		case 'F':
			s = "*******7*12*4567*12*4567*12*4567*123456701234567";
			break;
		case 'f':
			s = "012*45670******7*12*45670*23456701234567";
			break;
		case 'G':
			s = "0*****67*12345*7*12*45*7*12*45*70*2***6701234567";
			break;
		case 'g':
			s = "012**56701*34*6*01*34*6*012*4*6*01*****701234567";
			break;
		case 'H':
			s = "*******7012*4567012*4567012*4567*******701234567";
			break;
		case 'h':
			s = "*******7012*456701*3456701*34567012****701234567";
			break;
		case 'I':
			s = "*12345*7*******7*12345*701234567";
			break;
		case 'i':
			s = "*1*****701234567";
			break;
		case 'J':
			s = "*1234*67*12345*7*12345*7*12345*7******6701234567";
			break;
		case 'j':
			s = "012345*70123456**1*****701234567";
			break;
		case 'K':
			s = "*******7012*456701*3*5670*234*67*12345*701234567";
			break;
		case 'k':
			s = "*******70123*567012*4*6701*345*701*345*701234567";
			break;
		case 'L':
			s = "*******7012345*7012345*7012345*7012345*701234567";
			break;
		case 'l':
			s = "*******701234567";
			break;
		case 'M':
			s = "*******70*23456701**45670*234567*******701234567";
			break;
		case 'm':
			s = "01*****7012*456701*34567012****7012*456701*34567012****701234567";
			break;
		case 'N':
			s = "*******70*23456701***56701234*67*******701234567";
			break;
		case 'n':
			s = "01*****7012*456701*3456701*34567012****701234567";
			break;
		case 'O':
			s = "0*****67*12345*7*12345*7*12345*70*****6701234567";
			break;
		case 'o':
			s = "012***6701*345*701*345*701*345*7012***6701234567";
			break;
		case 'P':
			s = "*******7*12*4567*12*4567*12*45670**3456701234567";
			break;
		case 'p':
			s = "01******012*4*6701*345*701*345*7012***6701234567";
			break;
		case 'Q':
			s = "0*****67*12345*7*123*5*7*1234*670****5*701234567";
			break;
		case 'q':
			s = "012***6701*345*701*345*7012*4*6701******01234567";
			break;
		case 'R':
			s = "*******7*12*4567*12**567*12*4*670**345*701234567";
			break;
		case 'r':
			s = "01*****701*34567012*456701234567";
			break;
		case 'S':
			s = "0**34*67*12*45*7*12*45*7*12*45*70*23**6701234567";
			break;
		case 's':
			s = "012*456701*3*5*701*3*5*701*3*5*701234*6701234567";
			break;
		case 'T':
			s = "*1234567*1234567*******7*1234567*123456701234567";
			break;
		case 't':
			s = "012*4567******67012*45*701234567";
			break;
		case 'U':
			s = "******67012345*7012345*7012345*7******6701234567";
			break;
		case 'u':
			s = "01****67012345*7012345*701234*6701*****701234567";
			break;
		case 'V':
			s = "*****56701234*67012345*701234*67*****56701234567";
			break;
		case 'v':
			s = "01**45670123**67012345*70123**6701**456701234567";
			break;
		case 'W':
			s = "******67012345*70123**67012345*7******6701234567";
			break;
		case 'w':
			s = "01****67012345*70123**6701**45670123**67012345*701****6701234567";
			break;
		case 'X':
			s = "*12345*70**3**67012*45670**3**67*12345*701234567";
			break;
		case 'x':
			s = "01*345*7012*4*670123*567012*4*6701*345*701234567";
			break;
		case 'Y':
			s = "*12345670**34567012****70**34567*123456701234567";
			break;
		case 'y':
			s = "01**456*0123**6*012345*70123**6701**456701234567";
			break;
		case 'Z':
			s = "*1234**7*12**5*7*12*45*7*1**45*7**2345*701234567";
			break;
		case 'z':
			s = "01*345*701*34**701*3*5*701**45*701*345*701234567";
			break;
		case '0':
			s = "0*****67*123*5*7*12*45*7*1*345*70*****6701234567";
			break;
		case '1':
			s = "01*345670*2345*7*******7012345*701234567";
			break;
		case '2':
			s = "0*234**7*123*5*7*12*45*7*12*45*70**345*701234567";
			break;
		case '3':
			s = "0*234*67*12345*7*12*45*7*12*45*70**3**6701234567";
			break;
		case '4':
			s = "012*456701**45670*2*4567*******7012*456701234567";
			break;
		case '5':
			s = "****4*67*12*45*7*12*45*7*12*45*7*123**6701234567";
			break;
		case '6':
			s = "0*****67*12*45*7*12*45*7*12*45*70*23**6701234567";
			break;
		case '7':
			s = "*1234567*1234567*12****7*1*34567**23456701234567";
			break;
		case '8':
			s = "0**3**67*12*45*7*12*45*7*12*45*70**3**6701234567";
			break;
		case '9':
			s = "0**34*67*12*45*7*12*45*7*12*45*70*****6701234567";
			break;
		case '+':
			s = "012*4567012*45670*****67012*4567012*456701234567";
			break;
		case '-':
			s = "012*4567012*4567012*4567012*4567012*456701234567";
			break;
		case '.':
			s = "012345*701234567";
			break;
		case ',':
			s = "0123456*012345*701234567";
			break;
		case ';':
			s = "0123456*0123*5*701234567";
			break;
		case '?':
			s = "0*234567*1234567*123*5*7*12*45670**3456701234567";
			break;
		case '!':
			s = "*****5*701234567";
			break;
		case ':':
			s = "01*3*56701234567";
			break;
//		case '�':
//			s = "01*****70*23*567*123*567*******7*12*45*7*12*45*7*12345*701234567";
//			break;
//		case '�':
//			s = "01*34*6701*3*5*701*3*5*7012****701*3**6701*3*5*7012**5*701234567";
//			break;
//		case '�':
//			s = "012345*70*****67*123*5*7*12*45*7*1*345*70*****67*123456701234567";
//			break;
//		case '�':
//			s = "012***6*01*34**701*3*5*701**45*70*2***6701234567";
//			break;
//		case '�':
//			s = "0123***7012*4*67*1*34*67012*4*670123***701234567";
//			break;
//		case '�':
//			s = "01*34*6701*3*5*7*1*3*5*701*34*67012****701234567";
//			break;
//		case '�':
//			s = "0123***7*12*4*6701*34*67*12*4*670123***701234567";
//			break;
//		case '�':
//			s = "01*34*67*1*3*5*701*3*5*7*1*34*67012****701234567";
//			break;
//		case '�':
//			s = "012***67*1*345*701*345*7*1*345*7012***6701234567";
//			break;
//		case '�':
//			s = "012***67*1*345*701*345*7*1*345*7012***6701234567";
//			break;
//		case '�':
//			s = "01****67*12345*7012345*7*12345*701****6701234567";
//			break;
//		case '�':
//			s = "01****67*12345*7012345*7*1234*6701*****701234567";
//			break;
//		case '�':
//			s = "0********12*4*67*12*45*7*12*45*70**3**6701234567";
//			break;
		case '*':
			s = "012*45*70123**6701***5670123**67012*45*701234567";
			break;
		case '\'':
			s = "01*34567**23456701234567";
			break;
		case '%':
			s = "**234**7**23*567012*456701*34**7**234**701234567";
			break;
		case '(':
			s = "0******7*123456*01234567";
			break;
		case ')':
			s = "*123456*0******701234567";
			break;
		case '/':
			s = "01234**701***567**234567";
			break;
		default:
			s = "";
			break;
		}
		return s;
	}

	
	public void StartWriting(String s) {
		pist = "0123456701234567012345670123456701234567012345670123456701234567";
		for (int i = 0; i < s.length(); i++) {
			pist += getPixelSet(s.charAt(i));
		}
		ps = new LEDPixelSet[64];
		for (int i = 0; i < 64; i++) {
			ps[i] = new LEDPixelSet();
			ps[i].set((i % 8), (i / 8), 0, 0, 0);
		}
		startno = 0;
		scheduler.addToQueueLast("LEDMatrixTickerWait");
	}
	
	public void WritingStep() {
		if (startno < pist.length()) {
			for (int i = 0; i < 64; i++) {
				if ((startno + i < pist.length())
						&& (pist.charAt(startno + i) == '*')) {
					ps[i].setRed(red);
					ps[i].setGreen(green);
					ps[i].setBlue(blue);
				} else {
					ps[i].setRed(0);
					ps[i].setGreen(0);
					ps[i].setBlue(0);
				}
			}
			startno += 8;
			matrix.writeMatrix(ps);
			scheduler.addToQueueLast("LEDMatrixTickerWait");
		}
		else {
			scheduler.addToQueueLast("LEDMatrixTickerFinished");			
		}
	}


}
