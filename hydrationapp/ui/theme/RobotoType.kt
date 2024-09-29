package com.example.hydrationapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hydrationapp.R


val Roboto = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_bold)
)

object FontSize {
    val TopBarTitle = 17.sp
    val TopBarButtons = 17.sp
    val Body = 14.sp
    val GeneralButtons = 16.sp
    val Charts = 11.sp
    val GlassPercentage = 36.sp
    val DailyGoal = 36.sp
    val ContainerSize = 36.sp
    val DailyHydration = 18.sp
    val List = 16.sp
    val Navigation = 11.sp
}

interface Type {
    val TopBarTitle: TextStyle
    val TopBarButtons: TextStyle
    val Body: TextStyle
    val GeneralButtons: TextStyle
    val Chart: TextStyle
    val GlassPercentage: TextStyle
    val DailyGoal: TextStyle
    val ContainerSize: TextStyle
    val DailyHydration: TextStyle
    val List: TextStyle
    val ListSpan: SpanStyle
    val Navigation: TextStyle
    val BodySpan: SpanStyle
    val Settings: TextStyle
}


object RobotoType : Type {
    override val TopBarTitle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.TopBarTitle,
        fontWeight = FontWeight.SemiBold
    )
    override val TopBarButtons = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.TopBarButtons,
        fontWeight = FontWeight.Normal
    )
    override val Body: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.Body,
        fontWeight = FontWeight.Normal
    )
    override val GeneralButtons: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.GeneralButtons,
        fontWeight = FontWeight.SemiBold
    )
    override var Chart: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.Charts,
        fontWeight = FontWeight.Normal
    )
    override val GlassPercentage: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.GlassPercentage,
        fontWeight = FontWeight.Bold
    )
    override val DailyGoal: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.DailyGoal,
        fontWeight = FontWeight.Light
    )
    override val ContainerSize: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.ContainerSize,
        fontWeight = FontWeight.Light
    )
    override val DailyHydration: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.DailyHydration,
        fontWeight = FontWeight.SemiBold
    )

    override val List: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.List,
        fontWeight = FontWeight.Normal
    )

    override val ListSpan: SpanStyle = SpanStyle(
        fontSize = FontSize.List,
        fontWeight = FontWeight.Bold,
        fontFamily = Roboto
    )
    override val Navigation: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontSize = FontSize.Navigation,
        fontWeight = FontWeight.Normal
    )
    override val BodySpan: SpanStyle = SpanStyle(
        fontSize = FontSize.Body,
        fontWeight = FontWeight.Normal,
        fontFamily = Roboto
    )
    override val Settings: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        color = Color.Gray
    )

}

@Composable
fun TextStyle.glassPercentageDefaultColor() = this.copy(color = colorResource(id = R.color.green))


@Composable
fun TextStyle.generalButtonsDefaultColor() = this.copy(color = colorResource(id = R.color.black))

@Composable
fun TextStyle.dailyGoalDefaultColor() = this.copy(color = colorResource(id = R.color.white))

@Composable
fun TextStyle.bodyDefaultColor() = this.copy(color = colorResource(id = R.color.white))

@Composable
fun TextStyle.chartsDefaultColor() = this.copy(color = colorResource(id = R.color.white))

@Composable
fun TextStyle.listDefaultColor() = this.copy(color = colorResource(id = R.color.white))
