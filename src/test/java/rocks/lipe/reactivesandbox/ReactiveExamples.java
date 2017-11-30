package rocks.lipe.reactivesandbox;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
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

		log.info(person.sayMyName());
		assertThat(person.getFirstName(), is(equalTo("Felipe")));
		assertThat(person.getLastName(), is(equalTo("Faria")));
	}

	@Test
	public void monoTransform() {
		Mono<Person> personMono = Mono.just(paula);

		CommandPerson command = personMono.map(person -> {
			return new CommandPerson(person);
		}).block();

		log.info(command.sayMyName());
		assertThat(command.getFirstName(), is(equalTo("Paula")));
		assertThat(command.getLastName(), is(equalTo("Yamada")));
	}

	@Test(expected = NullPointerException.class)
	public void monoFilter() {
		Mono<Person> personMono = Mono.just(eduardo);

		Person p = personMono.filter(person -> person.getFirstName().equalsIgnoreCase("felipe")).block();

		log.info(p.sayMyName());
	}

}
