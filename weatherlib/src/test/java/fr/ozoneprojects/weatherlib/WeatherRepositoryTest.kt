package fr.ozoneprojects.weatherlib

import fr.ozoneprojects.weatherlib.internal.OpenWeatherMapApi
import fr.ozoneprojects.weatherlib.internal.WeatherRepositoryImpl
import fr.ozoneprojects.weatherlib.models.Error
import fr.ozoneprojects.weatherlib.models.Success
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse
import fr.ozoneprojects.weatherlib.utils.JsonUtils.moshi
import fr.ozoneprojects.weatherlib.utils.readFileAsTextUsingInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

@ExperimentalCoroutinesApi
internal class WeatherRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val parisCoordinates: Pair<Double, Double> = 48.8534 to 2.3488

    private lateinit var repository: WeatherRepository
    private lateinit var mockedOpenWeatherMapApi: OpenWeatherMapApi

    private val apiResponseSuccess: OpenWeatherOneCallResponse =
        moshi.adapter(OpenWeatherOneCallResponse::class.java)
            .fromJson(readFileAsTextUsingInputStream("openweathermap_response.json"))!!

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockedOpenWeatherMapApi = Mockito.mock(OpenWeatherMapApi::class.java)
        repository = WeatherRepositoryImpl("", mockedOpenWeatherMapApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `GIVEN onecall request WHEN a successful response returned from api call THEN repository should return a Success with weather conditions`() =
        runBlocking {
            BDDMockito.given(
                mockedOpenWeatherMapApi.getWeatherForecastForLocation(
                    parisCoordinates.first,
                    parisCoordinates.second,
                    ""
                )
            ).willReturn(apiResponseSuccess)

            val result = repository.getWeatherForecastForLocation(
                parisCoordinates.first,
                parisCoordinates.second
            )

            Assert.assertNotNull(result as Success)
            Assert.assertThat(result.value.current.weather.count(), `is`(not(0)))
            Assert.assertNotNull(result.value.current)
        }


    @Test
    fun `GIVEN onecall request WHEN response is not successful THEN an Error should be returned`() =
        runBlocking {
            BDDMockito.given(
                mockedOpenWeatherMapApi.getWeatherForecastForLocation(
                    parisCoordinates.first,
                    parisCoordinates.second,
                    ""
                )
            ).willReturn(null)

            val result = repository.getWeatherForecastForLocation(
                parisCoordinates.first,
                parisCoordinates.second
            )
            Assert.assertNotNull(result as Error)
            Assert.assertNotNull(result.throwable)
        }
}