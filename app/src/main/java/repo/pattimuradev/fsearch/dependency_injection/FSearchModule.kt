package repo.pattimuradev.fsearch.dependency_injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FSearchModule {
    @Singleton
    @Provides
    @Named("Coba String 1")
    fun provideCobaString() = "Hallo kak"
}