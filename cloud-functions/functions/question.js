export class Question{

    constructor(id,question,answer,help, solveMethod){
        question = question;
        answer = answer;
        id = id;
        help = help;
        solveMethod = solveMethod
    }

    getQuestionId() {
        return id;
    }

    getAnswer(){
        return answer;
    }

    getQuestion () {
        return question;
    }

    getHelp() {
        return help;
    }

    getSolveMethod() {
        return solveMethod;
    }

}