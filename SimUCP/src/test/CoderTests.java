package test;

import org.junit.Assert;
import org.junit.Test;

import utility.Coder;

public class CoderTests {
	@Test
	public void testRemoveBarckets() {
		Assert.assertEquals("eax", Coder.withoutBrackets("eax"));
		Assert.assertEquals("esp", Coder.withoutBrackets("[[esp]]"));
		Assert.assertEquals("100h", Coder.withoutBrackets("[100h]"));
		Assert.assertEquals("]text[", Coder.withoutBrackets("[[[]text[]]]"));
	}

	@Test
	public void testIsRegister() {
		Assert.assertEquals(true, Coder.isRegister("zero"));
		Assert.assertEquals(true, Coder.isRegister("eax"));
		Assert.assertEquals(true, Coder.isRegister("edx"));
		Assert.assertEquals(true, Coder.isRegister("esp"));
		Assert.assertEquals(true, Coder.isRegister("ebp"));
	}

	@Test
	public void testIsImmediate() {
		// signs
		Assert.assertEquals(true, Coder.isImmediate("1"));
		Assert.assertEquals(true, Coder.isImmediate("+1"));
		Assert.assertEquals(true, Coder.isImmediate("-1"));
		Assert.assertEquals(false, Coder.isImmediate("*1"));
		Assert.assertEquals(false, Coder.isImmediate("#1"));

		// base letters
		Assert.assertEquals(true, Coder.isImmediate("1"));
		Assert.assertEquals(true, Coder.isImmediate("1d"));
		Assert.assertEquals(true, Coder.isImmediate("1b"));
		Assert.assertEquals(true, Coder.isImmediate("1h"));
		Assert.assertEquals(false, Coder.isImmediate("1s"));
		Assert.assertEquals(false, Coder.isImmediate("1i"));

		// base validations
		Assert.assertEquals(true, Coder.isImmediate("0b"));
		Assert.assertEquals(true, Coder.isImmediate("100b"));
		Assert.assertEquals(false, Coder.isImmediate("300b"));
		Assert.assertEquals(false, Coder.isImmediate("1ffb"));
		Assert.assertEquals(false, Coder.isImmediate("ffb"));
		Assert.assertEquals(false, Coder.isImmediate("1zb"));
		Assert.assertEquals(false, Coder.isImmediate("zb"));

		Assert.assertEquals(true, Coder.isImmediate("0d"));
		Assert.assertEquals(true, Coder.isImmediate("100d"));
		Assert.assertEquals(true, Coder.isImmediate("300d"));
		Assert.assertEquals(false, Coder.isImmediate("1ffd"));
		Assert.assertEquals(false, Coder.isImmediate("ffd"));
		Assert.assertEquals(false, Coder.isImmediate("1zd"));
		Assert.assertEquals(false, Coder.isImmediate("zd"));

		Assert.assertEquals(true, Coder.isImmediate("0h"));
		Assert.assertEquals(true, Coder.isImmediate("100h"));
		Assert.assertEquals(true, Coder.isImmediate("300h"));
		Assert.assertEquals(true, Coder.isImmediate("1ffh"));
		Assert.assertEquals(true, Coder.isImmediate("ffh"));
		Assert.assertEquals(false, Coder.isImmediate("1zh"));
		Assert.assertEquals(false, Coder.isImmediate("zh"));
	}

	@Test
	public void testRegisterToIndex() {
		Assert.assertEquals(0, Coder.toRegIndex("zero"));
		Assert.assertEquals(1, Coder.toRegIndex("eax"));
		Assert.assertEquals(4, Coder.toRegIndex("edx"));
		Assert.assertEquals(5, Coder.toRegIndex("esp"));
		Assert.assertEquals(6, Coder.toRegIndex("ebp"));
	}

	@Test
	public void testValidate() {
		Assert.assertEquals("none", Coder.validate("None"));
		Assert.assertEquals("noop", Coder.validate("NoOp"));

		Assert.assertEquals("add ecx 1", Coder.validate("adD EcX 1D"));
		Assert.assertEquals("sub [eax] 100h", Coder.validate("sUb [[eAx]] [100H]"));
		Assert.assertEquals("dec eax", Coder.validate("dec eax"));
		Assert.assertEquals("inc [esp]", Coder.validate("iNc [eSp]"));
		Assert.assertEquals("not [ebx]", Coder.validate("not [EBX]"));
		Assert.assertEquals("neg edx", Coder.validate("neg eDx"));
		Assert.assertEquals("mov ebx eax", Coder.validate("mOv eBX eAX"));
		Assert.assertEquals("shl [esp] 1h", Coder.validate("sHl [[[ESp]]] 1H"));
		Assert.assertEquals("sar [edx] 100b", Coder.validate("sar [edx] 100B"));
		Assert.assertEquals("sal ecx 3h", Coder.validate("sal ecx 3H"));
		Assert.assertEquals("ror ebx 2", Coder.validate("ROR ebx 2D"));
		Assert.assertEquals("xor [ebp] [eax]", Coder.validate("XOR [ebp] [eax]"));
	}

	@Test
	public void testConvert() {
		Assert.assertEquals("none", Coder.convert("none"));
		Assert.assertEquals("noop", Coder.convert("noop"));

		Assert.assertEquals("add ecx 1", Coder.convert("add ecx 1"));
		Assert.assertEquals("sub [eax] 100h", Coder.convert("sub [eax] 100h"));
		Assert.assertEquals("sub eax 1", Coder.convert("dec eax"));
		Assert.assertEquals("add [esp] 1", Coder.convert("inc [esp]"));
		Assert.assertEquals("xor [ebx] -1", Coder.convert("not [ebx]"));
		Assert.assertEquals("sub zero edx", Coder.convert("neg edx"));
		Assert.assertEquals("mov ebx eax", Coder.convert("mov ebx eax"));
		Assert.assertEquals("shl [esp] 1h", Coder.convert("shl [esp] 1h"));
		Assert.assertEquals("sar [edx] 100b", Coder.convert("sar [edx] 100b"));
		Assert.assertEquals("sal ecx 3h", Coder.convert("sal ecx 3h"));
		Assert.assertEquals("ror ebx 2", Coder.convert("ror ebx 2"));
		Assert.assertEquals("xor [ebp] [eax]", Coder.convert("xor [ebp] [eax]"));
	}

	@Test
	public void testEncode() {
		Assert.assertEquals(0X00000000, Coder.encode("none"));
		Assert.assertEquals(0X00000000, Coder.encode("noop"));

		Assert.assertEquals(0X03300001, Coder.encode("add ecx 1"));
		Assert.assertEquals(0X0f100100, Coder.encode("sub [eax] 100h"));
		Assert.assertEquals(0X0b100001, Coder.encode("sub eax 1"));
		Assert.assertEquals(0X07500001, Coder.encode("add [esp] 1"));
		Assert.assertEquals(0X3720ffff, Coder.encode("xor [ebx] -1"));
		Assert.assertEquals(0X08040000, Coder.encode("sub zero edx"));
		Assert.assertEquals(0X10210000, Coder.encode("mov ebx eax"));
		Assert.assertEquals(0X47500001, Coder.encode("shl [esp] 1h"));
		Assert.assertEquals(0X6f400004, Coder.encode("sar [edx] 100b"));
		Assert.assertEquals(0X63300003, Coder.encode("sal ecx 3h"));
		Assert.assertEquals(0X5b200002, Coder.encode("ror ebx 2"));
		Assert.assertEquals(0X36610000, Coder.encode("xor [ebp] [eax]"));
	}

	public CoderTests() {
	}
}
