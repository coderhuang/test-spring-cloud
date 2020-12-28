package toby.es.test.constants;

public final class RabbitMqConstant {

	private RabbitMqConstant() {
	}

	// ----------------------------统一常量begin---------------------

	// 统一前缀
	public static final String PREFIX = "marketingcenter.";
	// 死信队列统一后缀
	public static final String DEAD_LETTER_SUFFIX = ".dlq";

	// ----------------------------统一常量end---------------------

	// ----------------------------队列常量begin---------------------

	// 券活动-公共属性-队列
	public static final String COMMON_COUPON_SETTINGS_SYNC_QUEUE = PREFIX + "common.settings.sync.queue";
	// 券活动-公共属性-死信队列
	public static final String COMMON_COUPON_SETTINGS_SYNC_DLQ = COMMON_COUPON_SETTINGS_SYNC_QUEUE + DEAD_LETTER_SUFFIX;

	// 券活动-私有属性-队列
	public static final String PRIVATE_COUPON_SETTINGS_SYNC_QUEUE = PREFIX + "private.settings.sync.queue";
	// 券活动-私有属性-死信队列
	public static final String PRIVATE_COUPON_SETTINGS_SYNC_DLQ = PRIVATE_COUPON_SETTINGS_SYNC_QUEUE
			+ DEAD_LETTER_SUFFIX;

	// ----------------------------队列常量end---------------------

	// ----------------------------交换机常量begin---------------------

	// 默认交换机
	public static final String DEFAULT_EXCHANGE = PREFIX + "default.exchange";
	// 死信交换机
	public static final String DEAD_LETTER_EXCHANGE = PREFIX + "deadletter.exchange";

	// ----------------------------交换机常量end---------------------
}
