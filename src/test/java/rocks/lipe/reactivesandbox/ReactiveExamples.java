package rocks.lipe.reactivesandbox;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactiveExamples {

	Person paula = new Person("Paula", "Yamada");
	Person felipe = new Person("Felipe", "Faria");
	Person eduardo = new Person("Eduardo", "Belis");
	Person gabriel = new Person("Gabriel", "Viana");

	@Test
	public void monoTest() {
		Mono<Person> personMono = Mono.just(felipe);

		Person person = personMono.block();

		log.info("monoTest: " + person.sayMyName());
		assertThat(person.getFirstName(), is(equalTo("Felipe")));
		assertThat(person.getLastName(), is(equalTo("Faria")));
	}

	@Test
	public void monoTransform() {
		Mono<Person> personMono = Mono.just(paula);

		CommandPerson command = personMono.map(person -> {
			return new CommandPerson(person);
		}).block();

		log.info("monoTransform: " + command.sayMyName());
		assertThat(command.getFirstName(), is(equalTo("Paula")));
		assertThat(command.getLastName(), is(equalTo("Yamada")));
	}

	@Test(expected = NullPointerException.class)
	public void monoFilter() {
		Mono<Person> personMono = Mono.just(eduardo);

		Person p = personMono.filter(person -> person.getFirstName().equalsIgnoreCase("felipe")).block();

		log.info(p.sayMyName());
	}

	@Test
	public void fluxTest() {
		Flux<Person> people = Flux.just(paula, felipe, eduardo, gabriel);

		people.subscribe(person -> log.info("fluxTest: " + person.sayMyName()));
	}

	@Test
	public void fluxFilterTest() {
		Flux<Person> people = Flux.just(paula, felipe, eduardo, gabriel);

		people.filter(person -> person.getFirstName().equalsIgnoreCase(paula.getFirstName()))
				.subscribe(person -> log.info("fluxFilterTest: " + person.sayMyName()));
	}

	@Test
	public void fluxDelayNoOutputTest() {

		Flux<Person> people = Flux.just(paula, felipe, eduardo, gabriel);

		people.delayElements(Duration.ofSeconds(1))
				.subscribe(person -> log.info("fluxDelayNoOutputTest: " + person.sayMyName()));

	}

	@Test
	public void fluxDelayTest() throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(1);

		Flux<Person> people = Flux.just(paula, felipe, eduardo, gabriel);

		people.delayElements(Duration.ofSeconds(1)).doOnComplete(countDownLatch::countDown)
				.subscribe(person -> log.info("fluxDelayNoOutputTest: " + person.sayMyName()));

		countDownLatch.await();
	}

	@Test
	public void fluxFilterDelayTest() throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(1);

		Flux<Person> people = Flux.just(paula, felipe, eduardo, gabriel);

		people.delayElements(Duration.ofSeconds(1)).filter(person -> person.getFirstName().contains("l"))
				.doOnComplete(countDownLatch::countDown)
				.subscribe(person -> log.info("fluxDelayNoOutputTest: " + person.sayMyName()));

		countDownLatch.await();
	}
}
