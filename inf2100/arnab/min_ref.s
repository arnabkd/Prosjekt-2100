        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # b =
        movl    $0,%eax                 # 0
        pushl   %eax                    
        movl    $3,%eax                 # 3
        leal    -8(%ebp),%edx           # x[...] = 
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      
.exit$main:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
