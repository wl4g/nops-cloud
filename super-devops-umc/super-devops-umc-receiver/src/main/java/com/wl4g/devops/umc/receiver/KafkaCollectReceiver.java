package com.wl4g.devops.umc.receiver;

import com.wl4g.devops.common.bean.umc.model.physical.Physical;
import com.wl4g.devops.common.bean.umc.model.third.Kafka;
import com.wl4g.devops.common.bean.umc.model.third.Redis;
import com.wl4g.devops.common.bean.umc.model.third.Zookeeper;
import com.wl4g.devops.common.bean.umc.model.virtual.Docker;
import com.wl4g.devops.common.utils.serialize.JacksonUtils;
import com.wl4g.devops.umc.store.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

import static com.wl4g.devops.common.constants.UMCDevOpsConstants.*;
import static com.wl4g.devops.umc.config.UmcReceiveAutoConfiguration.BEAN_KAFKA_BATCH_FACTORY;

/**
 * KAFKA collection receiver
 * 
 * @author wangl.sir
 * @version v1.0 2019年6月17日
 * @since
 */
public class KafkaCollectReceiver extends AbstractCollectReceiver {

	public KafkaCollectReceiver(PhysicalMetricStore pStore, VirtualMetricStore vStore, RedisMetricStore rStore, ZookeeperMetricStore zStore, KafkaMetricStore kStore) {
		super(pStore, vStore, rStore,zStore,kStore);
	}

	/**
	 * Receiving consumer messages on multiple topics
	 * 
	 * @param records
	 * @param ack
	 */
	@KafkaListener(topicPattern = TOPIC_RECEIVE_PATTERN, containerFactory = BEAN_KAFKA_BATCH_FACTORY)
	public void onMultiReceive(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
		try {
			if (log.isInfoEnabled()) {
				log.info("Consumer records for - {}", records);
			}

			// Process
			doProcess(records, new MultiAcknowledgmentState(ack));
		} catch (Exception e) {
			log.error("", e);
		} finally {
			// Echo
			// ack.acknowledge();
		}
	}

	/**
	 * UMC agent metric processing.
	 * 
	 * @param records
	 * @param state
	 */
	private void doProcess(List<ConsumerRecord<String, String>> records, MultiAcknowledgmentState state) {
		//
		// TODO
		//
		for(ConsumerRecord<String, String> consumerRecord : records){
			log.info("listen kafka message"+consumerRecord.value());
			String key = consumerRecord.key();
			String value = consumerRecord.value();

			switch (key){
				case URI_PHYSICAL:
					Physical physical = JacksonUtils.parseJSON(value, Physical.class);
					putPhysical(physical);
					break;
				case URI_VIRTUAL_DOCKER:
					Docker docker = JacksonUtils.parseJSON(value, Docker.class);
					putVirtualDocker(docker);
					break;
				case URI_REDIS:
					Redis redis = JacksonUtils.parseJSON(value, Redis.class);
					putRedis(redis);
					break;
				case URI_ZOOKEEPER:
					Zookeeper zookeeper = JacksonUtils.parseJSON(value, Zookeeper.class);
					putZookeeper(zookeeper);
					break;
				case URI_KAFKA:
					Kafka kafka = JacksonUtils.parseJSON(value, Kafka.class);
					putKafka(kafka);
					break;
				default:
					throw new UnsupportedOperationException("unsupport this type");
			}
		}
		state.completed();
	}

	/**
	 * Multiple ACK completion state
	 * 
	 * @author wangl.sir
	 * @version v1.0 2019年6月18日
	 * @since
	 */
	public static class MultiAcknowledgmentState {

		final private Acknowledgment ack;

		public MultiAcknowledgmentState(Acknowledgment ack) {
			super();
			this.ack = ack;
		}

		public void completed() {
			ack.acknowledge();
		}

	}

}
