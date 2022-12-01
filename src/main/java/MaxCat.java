
public record MaxCat(MaxCategory maxCategory,
                     MaxYearCategory maxYearCategory,
                     MaxMonthCategory maxMonthCategory,
                     MaxDayCategory maxDayCategory) {

}
 record MaxCategory(String category, int sum) {

}
record MaxYearCategory(String category, int sum) {
}

record MaxMonthCategory(String category, int sum) {
}

record MaxDayCategory(String category, int sum) {
}