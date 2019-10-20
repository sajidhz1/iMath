const fs = require('fs');
const {Qs} = require('./question-list');


class QuestionService{

    constructor(){
        this.questionList = new Qs().getQs().list;
    }

    getAnswerForQuestion(id){
        for(let i=0;i<this.questionList.length;i++){
            if(this.questionList[i].id==id){
                return this.questionList[i].answer;
            }
        }
    }

    getHelpForQuestion(id){
        for(let i=0;i<this.questionList.length;i++){
            if(this.questionList[i].id==id){
                return this.questionList[i].help;
            }
        }
    }

    getAnswerForQuestion(id){
        for(let i=0;i<this.questionList.length;i++){
            if(this.questionList[i].id==id){
                return this.questionList[i].answer;
            }
        }
    }

    getSolveMethodForQuestion(id){
        for(let i=0;i<this.questionList.length;i++){
            if(this.questionList[i].id==id){
                return this.questionList[i].solveMethod;
            }
        }
    }

    getNextQuestion(id){
        for(let i=0;i<this.questionList.length;i++){
            if(this.questionList[i].id==id){
                return this.questionList[i].question;
            }
        }
        return "Great! You have completed the question list. Let's see how much are correct out of the given answers..";
    }

    getLast(){
        return this.questionList[this.questionList.length-1];
    }
}

module.exports = {QuestionService}