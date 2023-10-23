function stringNull(str){
    let result = '';
    if(str == null || str == undefined || str == 'null'){
        result = ''
    }else{
        result = str;
    }
    return result;
}

function isBlank(string) {
    return (!string || string.trim().length === 0);
}

const convertSnakeToCamel = (s) => {
    return s.replace(/([-_][a-z])/ig, ($1) => {
        return $1.toUpperCase()
            .replace('-', '')
            .replace('_', '');
    });
};