const syllables = ["re", "do", "mi", "ma", "fi", "fa", "fu", "jo", "rho", "bie", "zha"];

export const randomName = () => {
    let result = "";
    for (var i = 0; i < 3; i++) {
        result += syllables[Math.floor(Math.random() * syllables.length)];
    }
    return result.substring(0, 1).toLocaleUpperCase() + result.substring(1);
};

export const formatTime = time => {
    if (time == 0) {
        return "-";
    }
    function f(n) {
        return ("" + (n + 100)).substring(1);
    }
    const date = new Date(time);
    return date.getFullYear() + "-" + f(1 + date.getUTCMonth()) + "-" + f(date.getUTCDate()) + " " + f(date.getUTCHours()) + ":" + f(date.getUTCMinutes()) + ":" + f(date.getUTCSeconds());
};
