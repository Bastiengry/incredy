package fr.bgsoft.incredy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class IncredyApplication_FullTests {
	@Test
	public void shouldRunApplication() {
		try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
			final ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
			springApplicationMock.when(() -> SpringApplication.run(IncredyApplication.class, new String[] {}))
			.thenReturn(mockContext);

			IncredyApplication.main(new String[] {});

			springApplicationMock.verify(() -> SpringApplication.run(IncredyApplication.class, new String[] {}));
		}
	}
}
