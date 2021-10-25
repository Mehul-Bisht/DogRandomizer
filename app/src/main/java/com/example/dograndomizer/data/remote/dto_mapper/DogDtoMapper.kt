import com.example.dograndomizer.data.remote.dto.DogDto
import com.example.dograndomizer.domain.models.Dog

/**
 * Created by Mehul Bisht on 24-10-2021
 */

fun DogDto.toDog(): Dog {
    return Dog(
        imageUrl = this.message,
        id = this.message.hashCode()
    )
}