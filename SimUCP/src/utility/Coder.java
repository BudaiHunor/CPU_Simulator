package utility;

import java.util.Arrays;

public class Coder {
	static public final String[] INSTRUCTIONS = { "add", "sub", "mov", "shl", "shr", "sal", "sar", "rol", "ror", "and",
			"or", "xor" };
	static public final String[] SPECIAL_INSTRUCTIONS = { "inc", "dec", "neg", "not" };
	static public final String[] REGISTERS = { "zero", "eax", "ebx", "ecx", "edx", "esp", "ebp" };

	private interface Condition {
		boolean condition(char c);
	}

	static public boolean bracketCondition(String str) {
		return (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']');
	}

	static public String withoutBrackets(String str) {
		if (Coder.bracketCondition(str)) {
			return Coder.withoutBrackets(str.substring(1, str.length() - 1));
		}
		return str;
	}

	static public boolean isRegister(String str) {
		return Arrays.asList(Coder.REGISTERS).contains(Coder.withoutBrackets(str));
	}

	static public boolean isImmediate(String str) {
		str = Coder.withoutBrackets(str);

		// add the plus sign to the front if it's missing
		// (presumed to be a valid positive immediate)
		if (str.charAt(0) != '-' && str.charAt(0) != '+') {
			str = "+" + str;
		}

		// compute the base
		int base = 10;
		if (str.charAt(str.length() - 1) == 'b') {
			base = 2;
		}
		if (str.charAt(str.length() - 1) == 'h') {
			base = 16;
		}
		// add the 'd' letter to the end for decimal values
		if (base == 10 && str.charAt(str.length() - 1) != 'd') {
			str += "d";
		}

		// compute the condition based on the base
		Condition cond = null;
		switch (base) {
		case 2:
			cond = (c) -> ('0' <= c && c <= '1');
			break;
		case 16:
			cond = (c) -> ('0' <= c && c <= '9' || 'a' <= c && c <= 'f');
			break;
		default:
			cond = (c) -> ('0' <= c && c <= '9');
		}

		// check the condition without the front sign and the end base letter
		for (char c : str.substring(1, str.length() - 1).toCharArray()) {
			if (!cond.condition(c)) {
				return false;
			}
		}
		return true;
	}

	static public int toOPCode(String str) {
		switch (str) {
		case "add":
			return 0;
		case "sub":
			return 1;
		case "mov":
			return 2;
		case "and":
			return 4;
		case "or":
			return 5;
		case "xor":
			return 6;
		case "shl":
			return 8;
		case "shr":
			return 9;
		case "rol":
			return 10;
		case "ror":
			return 11;
		case "sal":
			return 12;
		case "sar":
			return 13;
		default:
			return -1;
		}
	}

	static public int toRegIndex(String reg) {
		return Arrays.asList(Coder.REGISTERS).indexOf(reg);
	}

	/**
	 * @param instr
	 * @return A clean instruction String if it's valid or null otherwise.
	 */
	static public String validate(String instr) {
		if (instr == null) {
			return null;
		}

		// check for no operation
		if (Arrays.asList(new String[] { "none", "noop" }).contains(instr.toLowerCase())) {
			return instr.toLowerCase();
		}

		String error = null;
		String[] strings = instr.toLowerCase().split(" ");

		// check the number of strings/arguments
		if (!(2 <= strings.length && strings.length <= 3)) {
			return error;
		}

		// check if string[0]/operation is valid
		if (!(Arrays.asList(Coder.INSTRUCTIONS).contains(strings[0])
				|| Arrays.asList(Coder.SPECIAL_INSTRUCTIONS).contains(strings[0]))) {
			return error;
		}

		// check the number of strings/arguments depending on string[0]/operation
		if (Arrays.asList(Coder.INSTRUCTIONS).contains(strings[0])) {
			if (strings.length != 3) {
				return error;
			}
		}
		if (Arrays.asList(Coder.SPECIAL_INSTRUCTIONS).contains(strings[0])) {
			if (strings.length != 2) {
				return error;
			}
		}

		// check if strings[1]/operand 1 is register
		if (!Coder.isRegister(strings[1])) {
			return error;
		}

		// clean strings[1]/operand 1
		if (Coder.bracketCondition(strings[1])) {
			strings[1] = "[" + Coder.withoutBrackets(strings[1]) + "]";
		} else {
			strings[1] = Coder.withoutBrackets(strings[1]);
		}

		// if exists
		if (strings.length == 3) {
			// check if strings[2]/operand 2 is register or immediate
			if (!(Coder.isRegister(strings[2]) || Coder.isImmediate(strings[2]))) {
				return error;
			}

			// clean strings[2]/operand 2
			if (Coder.isRegister(strings[2])) {
				if (Coder.bracketCondition(strings[2])) {
					strings[2] = "[" + Coder.withoutBrackets(strings[2]) + "]";
				} else {
					strings[2] = Coder.withoutBrackets(strings[2]);
				}
			} else if (Coder.isImmediate(strings[2])) {
				strings[2] = Coder.withoutBrackets(strings[2]);

				// if the immediate string has the "d" character at the end (which is valid),
				// then remove it
				if (strings[2].charAt(strings[2].length() - 1) == 'd') {
					strings[2] = strings[2].substring(0, strings[2].length() - 1);
				}
			}
		}
		return String.join(" ", strings);
	}

	/**
	 * Warning! Method intended for valid and clean instruction String.
	 * 
	 * @param instr
	 * @return The actual instruction String.
	 */
	static public String convert(String instr) {
		if (instr == null) {
			return null;
		}

		// check for no operation
		if (Arrays.asList(new String[] { "none", "noop" }).contains(instr)) {
			return instr;
		}

		String error = null;
		String[] strings = instr.split(" ");

		// check if the operation is "special"
		if (Arrays.asList(Coder.SPECIAL_INSTRUCTIONS).contains(strings[0])) {
			if (strings.length != 2) {
				return error;
			}
			String s0 = null, s1 = null, s2 = null;
			switch (strings[0]) {
			case "inc":
				s0 = "add";
				s1 = strings[1];
				s2 = "1";
				break;
			case "dec":
				s0 = "sub";
				s1 = strings[1];
				s2 = "1";
				break;
			case "neg":
				s0 = "sub";
				s1 = "zero";
				s2 = strings[1];
				break;
			case "not":
				s0 = "xor";
				s1 = strings[1];
				s2 = "-1"; // "ffffffffh";
				break;
			}
			strings = new String[] { s0, s1, s2 };
		}
		return String.join(" ", strings);
	}

	/**
	 * Waring! Method intended for a "converted" instruction String.
	 * 
	 * @param instr
	 * @return
	 */
	static public int encode(String instr) {
		if (instr == null) {
			return -1;
		}

		// check for no operation
		if (Arrays.asList(new String[] { "none", "noop" }).contains(instr.toLowerCase())) {
			return 0;
		}

		// int error = -1;
		String[] strings = instr.toLowerCase().split(" ");
		boolean t1 = false, t2 = false, t3 = false;

		// check strings[1]/operand 1 type
		if (Coder.bracketCondition(strings[1])) {
			t1 = true;
		} else {
			t1 = false;
		}

		// check strings[2]/operand 2 type
		if (Coder.isImmediate(strings[2])) {
			t2 = true;
			t3 = true;
		} else if (Coder.bracketCondition(strings[2])) {
			t2 = true;
			t3 = false;
		} else {
			t2 = false;
			t3 = false;
		}

		// compute the instruction code
		int code = 0;
		code += Coder.toOPCode(strings[0]) << 27;
		code += t1 ? (1 << 26) : 0;
		code += t2 ? (1 << 25) : 0;
		code += t3 ? (1 << 24) : 0;
		code += Coder.toRegIndex(Coder.withoutBrackets(strings[1])) << 20;

		if (t3) {
			if (t2) {
				String immediate = strings[2].substring(0, strings[2].length() - 1);
				char ch = strings[2].charAt(strings[2].length() - 1);
				int base = 0;
				switch (ch) {
				case 'b':
					base = 2;
					break;
				case 'h':
					base = 16;
					break;
				case 'd':
					base = 10;
					break;
				default:
					base = 10;
					// add back the last digit that was cut instead of 'd' base letter
					immediate += strings[2].charAt(strings[2].length() - 1);
				}
				code += Integer.parseInt(immediate, base) & 0X0000FFFF;
			} else {
				code += 0; // immediate = 0 !!!
			}
		} else {
			if (t2) {
				code += Coder.toRegIndex(Coder.withoutBrackets(strings[2])) << 16;
			} else {
				code += Coder.toRegIndex(strings[2]) << 16;
			}
		}
		return code;
	}

	private Coder() {
	}
}
