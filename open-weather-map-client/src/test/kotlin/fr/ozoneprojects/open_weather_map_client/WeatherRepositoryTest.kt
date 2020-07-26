package fr.ozoneprojects.weatherlib

import fr.ozoneprojects.weatherlib.internal.OpenWeatherMapApi
import fr.ozoneprojects.weatherlib.internal.WeatherRepositoryImpl
import fr.ozoneprojects.weatherlib.models.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class WeatherRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val parisCoordinates: Pair<Double, Double> = 48.8534 to 2.3488

    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = WeatherRepositoryImpl(OpenWeatherMapApi.service)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test open weather map one call`() = runBlocking {
        val result = repository.getWeatherForecastForLocation(
            parisCoordinates.first,
            parisCoordinates.second,
            OpenWeatherMapApi.API_KEY
        )
        Assert.assertTrue(result is Success)
        Assert.assertNotNull((result as Success).value.current)
    }
}