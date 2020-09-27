package toby.es.test.common.utils;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author huangzhongjun
 * @date 2020-9-10
 * @description
 */
public final class RandomUtil {

	private RandomUtil() {
	}

	public static final String LOWER_STRING_CHARS = "abcdefghijklmnopqrstuvwxyz";

	public static final String RANDOM_STRING_NUMBERS = "0123456789";

	public static final String HIGHER_STRING_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final BiFunction<Integer, Boolean, String> RANDOM_STRING_FUNC = (size, containHigerChar) -> {

		final SecureRandom sr = new SecureRandom();
		final StringBuilder strb = new StringBuilder(size);
		int lowerCharsLength = LOWER_STRING_CHARS.length();
		int higherCharsLength = HIGHER_STRING_CHAR.length();
		int numbersLength = RANDOM_STRING_NUMBERS.length();
		// Java的假泛型真恶心，不能支持基础类型导致担心空指针(期待Valhalla)
		boolean containHigerCharPrimitive = Objects.nonNull(containHigerChar) ? containHigerChar.booleanValue() : false;
		for (int i = 0; i < size; i++) {
			if (sr.nextBoolean()) {
				strb.append(RANDOM_STRING_NUMBERS.charAt(sr.nextInt(numbersLength)));
			} else {
				if (containHigerCharPrimitive && sr.nextBoolean()) {
					strb.append(HIGHER_STRING_CHAR.charAt(sr.nextInt(higherCharsLength)));
				} else {
					strb.append(LOWER_STRING_CHARS.charAt(sr.nextInt(lowerCharsLength)));
				}
			}
		}
		return strb.toString();
	};

	public static final BiFunction<Integer, Boolean, String> IN_TURN_RANDOM_STRING_FUNC = (size, containHigerChar) -> {

		SecureRandom sr = new SecureRandom();
		StringBuilder strb = new StringBuilder(size);
		int lowerCharsLength = LOWER_STRING_CHARS.length();
		int higerCharsLength = HIGHER_STRING_CHAR.length();
		int numbersLength = RANDOM_STRING_NUMBERS.length();
		// Java的假泛型真恶心，不能支持基础类型导致担心空指针(期待Valhalla)
		boolean containHigerCharPrimitive = Objects.nonNull(containHigerChar) ? containHigerChar.booleanValue() : false;
		boolean charFlag = sr.nextBoolean();
		for (int i = 0; i < size; i++) {
			// 数字和字母交替
			if (!charFlag) {
				strb.append(RANDOM_STRING_NUMBERS.charAt(sr.nextInt(numbersLength)));
				charFlag = true;
			} else {

				if (containHigerCharPrimitive && sr.nextBoolean()) {
					strb.append(HIGHER_STRING_CHAR.charAt(sr.nextInt(higerCharsLength)));
				} else {
					strb.append(LOWER_STRING_CHARS.charAt(sr.nextInt(lowerCharsLength)));
				}
				charFlag = false;
			}
		}
		return strb.toString();
	};

	/**
	 * 生成随机字符串
	 * 
	 * @param size             长度
	 * @param containHigerChar 是否包含大写字母
	 * @param inTurnFlag       是否数字和字母交替
	 * @return
	 */
	public static String randomString(int size, boolean containHigerChar, boolean inTurnFlag) {

		if (size < 1) {
			throw new IllegalArgumentException("参数值不合适");
		}

		return inTurnFlag ? IN_TURN_RANDOM_STRING_FUNC.apply(size, containHigerChar)
				: RANDOM_STRING_FUNC.apply(size, containHigerChar);
	}
}
