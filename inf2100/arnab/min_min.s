        .globl  one                     
one:    pushl   %ebp                    # int one;
        movl    %esp,%ebp               
        movl    $1,%eax                 # 1
        jmp     .exit$one               # return-statement for one
.exit$one:                                
        popl    %ebp                    
        ret                             # end one
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $4,%esp                 # Allocate 4 bytes
        call    one                     # call one
        pushl   %eax                    
        call    one                     # call one
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    
        call    one                     # call one
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    
        movl    -4(%ebp),%eax           # Putting k in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    
        movl    $2,%eax                 # 2
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    
        movl    -4(%ebp),%eax           # Putting k in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        movl    %eax,-4(%ebp)           # k being assigned
.exit$main:                                
        addl    $4,%esp                 # Free 4 bytes
        popl    %ebp                    
        ret                             # end main
